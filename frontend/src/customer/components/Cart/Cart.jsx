import React, { useEffect, useState } from "react";
import CartItem from "./CartItem";
import { Button } from "@mui/material";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";

const Cart = () => {
  const { cartId } = useParams();
  const [cart, setCart] = useState({ id: 0, userId: 0, name: '', totalPrice: 0, product: [] });
  const navigate = useNavigate();

  const handleCheckout = () => {
    // Add any additional logic or API calls needed before navigating to /checkout

    // Navigate to the /checkout page
    navigate('/add/address');
  };

  const apiUrl = `http://localhost:8082/api/public/user/cart/${cartId}`;

  useEffect(() => {
    const fetchCart = async () => {
      try {
        const response = await axios.get(apiUrl);
        setCart(response.data);
        console.log("DATA", cart);
      } catch (error) {
        console.error('Error fetching cart:', error);
      }
    };

    fetchCart();
  }, [cartId]);

  return (
    <div>
      <div className="lg:grid grid-cols-3 lg:px-16 relative">
        <div className="lg:col-span-2 lg:px-5 bg-white">
          {Array.isArray(cart.product) &&
            cart.product.map((productItem) => (
              <CartItem key={productItem.id} item={productItem} />
            ))}
        </div>

        <div className="px-5 sticky top-0 h-[100vh] mt-5 lg:mt-0">
          <div className="border p-5 bg-white shadow-lg rounded-md">
            <p className="font-bold opacity-60 pb-4">PRICE DETAILS</p>
            <hr />

            <div className="space-y-3 font-semibold">
              <div className="flex justify-between pt-3 text-black">
                <span>Price</span>
                <span>₹{cart.totalPrice}</span>
              </div>
              <div className="flex justify-between">
                <span>Discount</span>
                <span className="text-green-700">₹--</span>
              </div>
              <div className="flex justify-between">
                <span>Delivery Charges</span>
                <span className="text-green-700">Free</span>
              </div>
              <hr />
              <div className="flex justify-between font-bold text-lg">
                <span>Total Amount</span>
                <span className="text-green-700">₹{cart.totalPrice}</span>
              </div>
            </div>

            <Button
              variant="contained"
              onClick={handleCheckout}
              type="submit"
              sx={{ padding: ".8rem 2rem", marginTop: "2rem", width: "100%" }}
            >
              Check Out
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;
