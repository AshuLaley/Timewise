package com.watcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.watcher.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

	//	@Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")

	@Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId  AND ci.product.id = :productId ")
	CartItem findCartItemByProductIdAndCartId(int cartId, int productId);

	@Modifying
	 @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId  AND ci.product.id = :productId ")
	    void deleteCartItemByProductIdAndCartId( int cartId,int productId);

	@Modifying
    @Query("DELETE FROM CartItem c WHERE c.cart.id = :cartId")
	void deleteByCartId(int cartId);
	 }
