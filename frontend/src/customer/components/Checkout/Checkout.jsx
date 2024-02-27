import React, { useEffect, useState } from 'react';
import { Button } from '@mui/material';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import CartItem from '../Cart/CartItem';

const Checkout = () => {
  const { cartId } = useParams();
  const [cart, setCart] = useState({ id: 0, userId: 0, name: '', totalPrice: 0, product: [] });
  const [userAddress, setUserAddress] = useState(null);
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [emailId, setEmailId] = useState("")
  //const [userid, setUserid] = useState('');
  const navigate = useNavigate();
 // let emailId = "";
  let userid='';
  let paymentMethod='cod';
  const handleCheckout = async () => {
    // Add any additional logic or API calls needed before navigating to /checkout
    console.log("2---------------------",emailId)
    //                                           public/users/{emailId}/carts/{cartId}/payments/{paymentMethod}
     await axios.post(`http://localhost:8082/api/public/users/${emailId}/carts/${cartId}/payments/${paymentMethod}`);
    // Simulate placing the order
    // Replace the following line with actual logic or API call
    await new Promise(resolve => setTimeout(resolve, 2000));

    // Show the confirmation bar
    setShowConfirmation(true);

    // Automatically redirect to homepage after 5 seconds
    setTimeout(() => {
      navigate('/');
    }, 5000);
  };
  useEffect(() => {
    // Retrieve data from local storage
    const storedData = JSON.parse(localStorage.getItem('data'));

    // Check if data and user object exi
      // Extract and set the userId
      userid=storedData.user.id
      setEmailId(storedData.user.email)
      console.log(storedData.user.id,"--------",emailId)
     // setUserid(storedData.user.id);
  }, []);

  useEffect(() => {
    const fetchCart = async () => {
      try {
        const cartResponse = await axios.get(`http://localhost:8082/api/public/user/cart/${cartId}`);
        setCart(cartResponse.data);

        // Fetch user address
        const userId = cartResponse.data.userId;
        const addressResponse = await axios.get(`http://localhost:8082/api/public/user/address/${userid}`);
        setUserAddress(addressResponse.data);

      } catch (error) {
        console.error('Error fetching data:', error);
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
            <div>
              <h2 className="text-xl font-bold mb-2">Delivery Address</h2>
              {userAddress && (
                <div>
                  <p>{userAddress.street}, {userAddress.buildingName}</p>
                  <p>{userAddress.city}, {userAddress.state}, {userAddress.country} - {userAddress.pincode}</p>
                </div>
              )}
            </div>
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

            {!showConfirmation && (
              <Button
                variant="contained"
                onClick={handleCheckout}
                type="submit"
                sx={{ padding: ".8rem 2rem", marginTop: "2rem", width: "100%" }}
              >
                Place Order
              </Button>
            )}

            {showConfirmation && (
              <div className="bg-green-500 text-white p-4 mt-4 rounded-md">
                Your order has been placed successfully! Redirecting to homepage...
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Checkout;
