INSERT INTO OrderStatusEnum (OrderStatusEnumId, Name, Description) VALUES
(0, 'potential', 'Potential order acts as shopping cart content'),
(1, 'open', 'Order has been placed'),
(2, 'in progress', 'Order is progresssed and made ready for delivery'),
(3, 'delivered', 'Order has been delivered to destination address'),
(4, 'canceled', 'Order has been canceled'),
(5, 'returned', 'Merchendize has been returned by the customer');

INSERT INTO PaymentMethodEnum (PaymentMethodEnumId, Name, Description) VALUES
(0, 'Credit Card', ''),
(1, 'Bank transfer', ''),
(2, 'PayPal', ''),
(3, 'Cash on delivery', '');