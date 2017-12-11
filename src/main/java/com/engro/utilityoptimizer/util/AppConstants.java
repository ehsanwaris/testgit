package com.engro.utilityoptimizer.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Random;

public class AppConstants {

//	public static String[] tags = {"TE_WS_UNIT1_NET0","PT_WS_UNIT1_NET0","420TE001_UNIT1_NET0","420PT002_UNIT1_NET0","420TE002_UNIT1_NET0","420PT003_UNIT1_NET0",
//			"WS_HT_UNIT1_NET0","420NLS001A_A_UNIT1_NET0","420NLS001B_A_UNIT1_NET0","420NLS001C_A_UNIT1_NET0","420NLS001D_A_UNIT1_NET0","420NLS001E_A_UNIT1_NET0",
//			"203PT026_UNIT1_NET0","420NLS002A_I_UNIT1_NET0","420NLS002B_I_UNIT1_NET0","420NLS002C_I_UNIT1_NET0","420NLS002D_I_UNIT1_NET0","AH3_UA_UNIT1_NET0",
//			"2G_AI21_UNIT1_NET0"};
	
	public static final String STATUS_BAD = "bad";
	public static final String STATUS_GOOD = "good";
	public static final String STATUS_NEUTRAL = "neutral";
	public static final String STATUS_INACTIVE = "inactive";
	
	public static final String STATUS_NORMAL = "normal";
	public static final String STATUS_ABNORMAL = "abnormal";
	
	public static final String DIRECTION_INCREASE = "increase";
	public static final String DIRECTION_DECREASE = "decrease";
	public static final String DIRECTION_SHUTDOWN = "shutdown";
	public static final String DIRECTION_NA = "na";
	public static final String DEFAULT_RESPONSE = "{\"Fan_Status_C\":\"bad\",\"Fan_Status_D\":\"good\",\"Fan_Status_A\":\"good\",\"Fan_Status_B\":\"good\",\"Fan_Amperes_C\":14.22,\"Fan_Amperes_B\":16.12,\"Fan_Amperes_A\":15.66,\"Fan_Amperes_D\":14.17,\"Condenser_Vaccum\":-95.73,\"approach\":9.81,\"Liquid_Flow_OT\":18707.75,\"Supply_Temprature_Margin\":24.24,\"Bad_Pumps_Count\":1,\"Ambient_Pressure_Hpa\":1027.86,\"Ambient_Pressure_Kpa\":102.78,\"Main_Steam_Flow\":252.13,\"GT_Load_MW\":113.84,\"Pump_Direction_A\":\"increase\",\"Pump_Direction_B\":\"na\",\"Pump_Direction_C\":\"na\",\"Pump_Direction_D\":\"na\",\"Pump_Direction_E\":\"na\",\"Return_Pressure\":0.16,\"pump_flow_d\":3412.05,\"pump_flow_e\":3418.04,\"pump_flow_b\":3445.04,\"Bad_Fans_Count\":1,\"pump_flow_c\":3527.79,\"dollar_value\":48.5,\"fan_flow_c\":3204574.87,\"fan_flow_d\":0,\"fan_flow_a\":2270185.53,\"fan_flow_b\":2071964.87,\"Projected_Vacuum\":-96.21,\"pump_flow_a\":0,\"Achievable_Supply_Temperature\":17.08,\"Liquid_Flow\":13802.93,\"Pumps_Amperes_E\":31.63,\"Pumps_Amperes_D\":31.59,\"energyIndex\":0.11,\"range\":5.16,\"Pump_Status_C\":\"good\",\"Pump_Status_B\":\"good\",\"Gas_Flow_OT\":5440980.18,\"Pump_Status_E\":\"good\",\"Pump_Status_D\":\"good\",\"Pump_Status_A\":\"bad\",\"Return_Temperature\":28.05,\"Humidity\":34.57,\"Voltage\":10.78,\"Pumps_Amperes_C\":32.36,\"Gross_Load_MW\":191,\"STG_Load_MW\":76.44,\"Pumps_Amperes_B\":31.81,\"Pumps_Amperes_A\":0.01,\"Gas_Flow\":7546725.26,\"timestamp\":\"1502869499909\",\"Supply_Temperature\":22.89,\"maximum_dollar_value\":380.61,\"Fan_Direction_C\":\"decrease\",\"Fan_Direction_B\":\"na\",\"Fan_Direction_A\":\"na\",\"Power_Factor\":0.75,\"lg_ratio\":0.0018,\"Ambient_Temperature\":22,\"wet_bulb_temp\":13.08,\"Supply_Pressure\":0.25,\"Fan_Direction_D\":\"na\"}";

	
	public static final String[] tags = {"ew"};
	
	/***
	 * Round off to two decimal Places
	 * https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	protected static Random random = new Random();
	/***
	 * Generate random double in range   
	 * link-> https://stackoverflow.com/questions/9723765/generating-a-random-double-number-of-a-certain-range-in-java
	 * @param min
	 * @param max
	 * @return
	 */
	public static double randomInRange(double min, double max) {
	  double range = max - min;
	  double scaled = random.nextDouble() * range;
	  double shifted = scaled + min;
	  return shifted; // == (rand.nextDouble() * (max-min)) + min;
	}
	}
