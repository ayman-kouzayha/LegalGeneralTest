package com.example.LegalGeneralTest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/change")
public class CoinUnitController {
    private static final String RESPONSE_COMPLETED_SUCCESSFULLY = "Request completed successfully";
    private static final String RESPONSE_BAD_DATA = "Bad data!";
    private static final String RESPONSE_SERVER_ERROR = "Server error!";

    private TreeMap<Integer, CoinUnit> treeMapCoins;

    @PostMapping("/init")
    private ResponseEntity<String> initializeChange(@Validated @RequestBody List<CoinUnit> listCoins) {
        treeMapCoins = new TreeMap<>();
        if (listCoins == null || listCoins.size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RESPONSE_BAD_DATA);
        }
        try {
            for (CoinUnit coinUnit : listCoins) {
                treeMapCoins.put(coinUnit.getValue(), coinUnit);
            }
            return ResponseEntity.status(HttpStatus.OK).body(RESPONSE_COMPLETED_SUCCESSFULLY);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RESPONSE_SERVER_ERROR + " " + e.getMessage());
        }
    }

    @PostMapping("/deposit")
    private ResponseEntity<String> addChange(@Validated @RequestBody List<CoinUnit> listCoins) {
        if (treeMapCoins == null) {
            treeMapCoins = new TreeMap<>();
        }
        if (listCoins == null || listCoins.size() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RESPONSE_BAD_DATA);
        }
        try {
            for (CoinUnit coinUnit : listCoins) {
                if (treeMapCoins.containsKey(coinUnit.getValue())) {
                    CoinUnit currentCoinUnit = treeMapCoins.get(coinUnit.getValue());
                    currentCoinUnit.setQuantity(currentCoinUnit.getQuantity() + coinUnit.getQuantity());
                    treeMapCoins.put(coinUnit.getValue(), currentCoinUnit);
                } else {
                    treeMapCoins.put(coinUnit.getValue(), coinUnit);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(RESPONSE_COMPLETED_SUCCESSFULLY);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RESPONSE_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{amount}")
    public ResponseEntity<CoinUnit[]> getChange(@PathVariable(value = "amount") int amount) {
        if (treeMapCoins == null || treeMapCoins.size() == 0 || amount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            CoinUnit[] result = calculateCoinChange(treeMapCoins, amount);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private CoinUnit[] calculateCoinChange(TreeMap<Integer, CoinUnit> treeMapCoins, int amount) {
        Integer[] coins = treeMapCoins.keySet().toArray(new Integer[0]);
        int[] limits = treeMapCoins.values().stream().mapToInt(CoinUnit::getQuantity).toArray();
        int[][] coinsUsed = new int[amount + 1][coins.length];
        int[] minCoins = new int[amount + 1];

        for (int i = 1; i <= amount; i++) {
            minCoins[i] = Integer.MAX_VALUE - 1;
        }

        for (int i = 0; i < coins.length; i++) {
            while (limits[i] > 0) {
                for (int j = amount; j >= 0; j--) {
                    int currAmount = j + coins[i];
                    if (currAmount <= amount) {
                        if (minCoins[currAmount] > minCoins[j] + 1) {
                            minCoins[currAmount] = minCoins[j] + 1;
                            System.arraycopy(coinsUsed[j], 0, coinsUsed[currAmount], 0, coinsUsed[j].length);
                            coinsUsed[currAmount][i] += 1;
                        }
                    }
                }
                limits[i] -= 1;
            }
        }
        if (minCoins[amount] == Integer.MAX_VALUE - 1) {
            return null;
        }
        ArrayList<CoinUnit> arrayListChangeCoins = new ArrayList<>();
        for (int i = 0; i < coinsUsed[amount].length; i++) {
            if (coinsUsed[amount][i] > 0) {
                CoinUnit currentCoin = treeMapCoins.get(coins[i]);
                currentCoin.setQuantity(currentCoin.getQuantity() - coinsUsed[amount][i]);
                CoinUnit clonedCoin = new CoinUnit(currentCoin);
                clonedCoin.setQuantity(coinsUsed[amount][i]);
                arrayListChangeCoins.add(clonedCoin);
            }
        }
        return arrayListChangeCoins.toArray(new CoinUnit[0]);
    }
}
