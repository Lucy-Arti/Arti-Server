package com.lucy.arti.vote.service;

import com.lucy.arti.clothes.domain.Clothes;
import com.lucy.arti.clothes.repository.ClothesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final ClothesRepository clothesRepository;
    public List<Clothes> getVoteList() {
        List<Clothes> allClothes = clothesRepository.findAll();
        int clothesSize = allClothes.size();
        List<Clothes> randClothes = new ArrayList<>();
        Random randInt = new Random();
        Set<Integer> integerSet = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            while (true) {
                int newInt = randInt.nextInt(clothesSize);
                if (!(integerSet.contains(newInt))){
                    integerSet.add(newInt);
                    randClothes.add(allClothes.get(newInt));
                    break;
                }

            }
        }
        return randClothes;
    }
}
