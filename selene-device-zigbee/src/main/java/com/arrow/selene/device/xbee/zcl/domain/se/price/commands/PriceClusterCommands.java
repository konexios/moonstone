package com.arrow.selene.device.xbee.zcl.domain.se.price.commands;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.arrow.selene.device.xbee.zcl.ClusterSpecificCommand;

public class PriceClusterCommands {
	public static final int PUBLISH_PRICE_COMMAND_ID = 0x00;
	public static final int PUBLISH_BLOCK_PERIOD_COMMAND_ID = 0x01;
	public static final int PUBLISH_CONVERSION_FACTOR_COMMAND_ID = 0x02;
	public static final int PUBLISH_CALORIFIC_VALUE_COMMAND_ID = 0x03;
	public static final int PUBLISH_TARIFF_INFORMATION_COMMAND_ID = 0x04;
	public static final int PUBLISH_PRICE_MATRIX_COMMAND_ID = 0x05;
	public static final int PUBLISH_BLOCK_THRESHOLDS_COMMAND_ID = 0x06;
	public static final int PUBLISH_CO2_VALUE_COMMAND_ID = 0x07;
	public static final int PUBLISH_TIER_LABELS_COMMAND_ID = 0x08;
	public static final int PUBLISH_BILLING_PERIOD_COMMAND_ID = 0x09;
	public static final int PUBLISH_CONSOLIDATED_BILL_COMMAND_ID = 0x0A;
	public static final int PUBLISH_CPP_EVENT_COMMAND_ID = 0x0B;
	public static final int PUBLISH_CREDIT_PAYMENT_COMMAND_ID = 0x0C;
	public static final int PUBLISH_CURRENCY_CONVERSION_COMMAND_ID = 0x0D;
	public static final int CANCEL_TARIFF_COMMAND_ID = 0x0E;

	public static final int GET_CURRENT_PRICE_COMMAND_ID = 0x00;
	public static final int GET_SCHEDULED_PRICES_COMMAND_ID = 0x01;
	public static final int PRICE_ACKNOWLEDGEMENT_COMMAND_ID = 0x02;
	public static final int GET_BLOCK_PERIODS_COMMAND_ID = 0x03;
	public static final int GET_CONVERSION_FACTOR_COMMAND_ID = 0x04;
	public static final int GET_CALORIFIC_VALUE_COMMAND_ID = 0x05;
	public static final int GET_TARIFF_INFORMATION_COMMAND_ID = 0x06;
	public static final int GET_PRICE_MATRIX_COMMAND_ID = 0x07;
	public static final int GET_BLOCK_THRESHOLDS_COMMAND_ID = 0x08;
	public static final int GET_CO2_VALUE_COMMAND_ID = 0x09;
	public static final int GET_TIER_LABELS_COMMAND_ID = 0x0A;
	public static final int GET_BILLING_PERIOD_COMMAND_ID = 0x0B;
	public static final int GET_CONSOLIDATED_BILL_COMMAND_ID = 0x0C;
	public static final int CPP_EVENT_RESPONSE_COMMAND_ID = 0x0D;
	public static final int GET_CREDIT_PAYMENT_COMMAND_ID = 0x0E;
	public static final int GET_CURRENCY_CONVERSION_COMMAND_COMMAND_ID = 0x0F;
	public static final int GET_TARIFF_CANCELLATION_COMMAND_ID = 0x10;

	public static final Map<Integer, ImmutablePair<String, Class<? extends ClusterSpecificCommand<?>>>> ALL_RECEIVED =
			new LinkedHashMap<>();
	public static final Map<Integer, String> ALL_GENERATED = new LinkedHashMap<>();

	static {
		ALL_GENERATED.put(PUBLISH_PRICE_COMMAND_ID, "Publish Price");
		ALL_GENERATED.put(PUBLISH_BLOCK_PERIOD_COMMAND_ID, "Publish Block Period");
		ALL_GENERATED.put(PUBLISH_CONVERSION_FACTOR_COMMAND_ID, "Publish Conversion Factor");
		ALL_GENERATED.put(PUBLISH_CALORIFIC_VALUE_COMMAND_ID, "Publish Calorific Value");
		ALL_GENERATED.put(PUBLISH_TARIFF_INFORMATION_COMMAND_ID, "Publish Tariff Information");
		ALL_GENERATED.put(PUBLISH_PRICE_MATRIX_COMMAND_ID, "Publish Price Matrix");
		ALL_GENERATED.put(PUBLISH_BLOCK_THRESHOLDS_COMMAND_ID, "Publish Block Thresholds");
		ALL_GENERATED.put(PUBLISH_CO2_VALUE_COMMAND_ID, "Publish CO2 Value");
		ALL_GENERATED.put(PUBLISH_TIER_LABELS_COMMAND_ID, "Publish Tier Labels");
		ALL_GENERATED.put(PUBLISH_BILLING_PERIOD_COMMAND_ID, "Publish Billing Period");
		ALL_GENERATED.put(PUBLISH_CONSOLIDATED_BILL_COMMAND_ID, "Publish Consolidated Bill");
		ALL_GENERATED.put(PUBLISH_CPP_EVENT_COMMAND_ID, "Publish Critical Peak Pricing Event");
		ALL_GENERATED.put(PUBLISH_CREDIT_PAYMENT_COMMAND_ID, "Publish Credit Payment");
		ALL_GENERATED.put(PUBLISH_CURRENCY_CONVERSION_COMMAND_ID, "Publish Currency Conversion");
		ALL_GENERATED.put(CANCEL_TARIFF_COMMAND_ID, "Cancel Tariff");

		ALL_RECEIVED.put(GET_CURRENT_PRICE_COMMAND_ID, new ImmutablePair<>("Get Current Price", GetCurrentPrice
				.class));
		ALL_RECEIVED.put(GET_SCHEDULED_PRICES_COMMAND_ID,
				new ImmutablePair<>("Get Scheduled Prices", GetScheduledPrices.class));
		ALL_RECEIVED.put(PRICE_ACKNOWLEDGEMENT_COMMAND_ID,
				new ImmutablePair<>("Price Acknowledgement", PriceAcknowledgment.class));
		ALL_RECEIVED.put(GET_BLOCK_PERIODS_COMMAND_ID,
				new ImmutablePair<>("Get Block Periods", GetBillingPeriod.class));
		ALL_RECEIVED.put(GET_CONVERSION_FACTOR_COMMAND_ID,
				new ImmutablePair<>("Get Conversion Factor", GetConversionFactor.class));
		ALL_RECEIVED.put(GET_CALORIFIC_VALUE_COMMAND_ID,
				new ImmutablePair<>("Get Calorific Value", GetCalorificValue.class));
		ALL_RECEIVED.put(GET_TARIFF_INFORMATION_COMMAND_ID,
				new ImmutablePair<>("Get Tariff Information", GetTariffInformation.class));
		ALL_RECEIVED.put(GET_PRICE_MATRIX_COMMAND_ID, new ImmutablePair<>("Get Price Matrix", GetPriceMatrix.class));
		ALL_RECEIVED.put(GET_BLOCK_THRESHOLDS_COMMAND_ID,
				new ImmutablePair<>("Get Block Thresholds", GetBlockThresholds.class));
		ALL_RECEIVED.put(GET_CO2_VALUE_COMMAND_ID, new ImmutablePair<>("Get CO2 Value", GetCo2Value.class));
		ALL_RECEIVED.put(GET_TIER_LABELS_COMMAND_ID, new ImmutablePair<>("Get Tier Labels", GetTierLabels.class));
		ALL_RECEIVED.put(GET_BILLING_PERIOD_COMMAND_ID,
				new ImmutablePair<>("Get Billing Period", GetBillingPeriod.class));
		ALL_RECEIVED.put(GET_CONSOLIDATED_BILL_COMMAND_ID,
				new ImmutablePair<>("Get Consolidated Bill", GetConsolidatedBill.class));
		ALL_RECEIVED.put(CPP_EVENT_RESPONSE_COMMAND_ID,
				new ImmutablePair<>("Critical Peak Pricing Event Response", CppEventResponse.class));
		ALL_RECEIVED.put(GET_CREDIT_PAYMENT_COMMAND_ID,
				new ImmutablePair<>("Get Credit Payment", GetCreditPayment.class));
		ALL_RECEIVED.put(GET_CURRENCY_CONVERSION_COMMAND_COMMAND_ID,
				new ImmutablePair<>("Get Currency Conversion", GetCurrencyConversion.class));
		ALL_RECEIVED.put(GET_TARIFF_CANCELLATION_COMMAND_ID,
				new ImmutablePair<>("Get Tariff Cancellation", GetTariffCancellation.class));
	}
}
