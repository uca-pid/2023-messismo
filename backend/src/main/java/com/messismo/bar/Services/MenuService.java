package com.messismo.bar.Services;

import com.messismo.bar.Entities.Product;
import com.messismo.bar.Repositories.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public ResponseEntity<?> getMenu(Long menuId) {
        try{
            if(menuRepository.findByMenuId(menuId).isPresent()) {
                Set<Product> products = menuRepository.findByMenuId(menuId).get().getProducts();
                return ResponseEntity.status(HttpStatus.OK).body(products);
            }
            else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Menu doesn't exists");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting the menu");

        }
    }
}
