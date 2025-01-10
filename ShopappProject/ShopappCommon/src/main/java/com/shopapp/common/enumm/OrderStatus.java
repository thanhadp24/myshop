package com.shopapp.common.enumm;

public enum OrderStatus {

	NEW {
		@Override
		public String defaultDescription() {
			return "The order was placed by the customer";
		}
	},
	CANCELLED {
		@Override
		public String defaultDescription() {
			return "Order was rejected";
		}
	},
	PROCCESSING {
		@Override
		public String defaultDescription() {
			return "Order is being proccessing";
		}
	},
	PACKAGED {
		@Override
		public String defaultDescription() {
			return "Products were packaged";
		}
	},
	PICKED {
		@Override
		public String defaultDescription() {
			return "Shipper picked the products";
		}
	},
	SHIPPING {
		@Override
		public String defaultDescription() {
			return "Shipper is delivering the products";
		}
	},
	DELIVERED {
		@Override
		public String defaultDescription() {
			return "Customer received the products";
		}
	},
	RETURNED {
		@Override
		public String defaultDescription() {
			return "Products were returned";
		}
	},
	PAID {
		@Override
		public String defaultDescription() {
			return "Customer has paid this order";
		}
	},
	REFUNDED {
		@Override
		public String defaultDescription() {
			return "Customer has been refunded";
		}
	};

	public abstract String defaultDescription();
}
