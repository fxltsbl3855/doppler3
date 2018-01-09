package com.sinoservices.doppler2.config;

/**
 * 
 * 
 * @author tengfei
 * @createtime 2014年11月13日 下午3:50:58
 * 
 */

public class CommonConfig {

	public static int TEST_AGE = 21;

	/** 代金券发放逻辑 */
	public static String COUPON_CHARGE_URL;

	/** 书籍目录服务 */
	public static String CPS_CATALOG_URL;
	
	/** 校验资产 */
	public static String CHECK_ASSET;

	/** 活动基本信息 */
	public static String CPS_CAMPAIGNS = "http://59.151.100.36:8070/cps/getCampaignBatch";

	/** 书籍详情 */
	public static String BOOK_DETAIL_BATCH_URL = "";
	
	/**acc 文件地址 */
	public static String ACC_FILE_URL = "";
	
	

	/** common kafka 相关配置 begin */
	public static String KAFKA_COMMON_SERIALIZER_CLAZZ_VALUE = "kafka.serializer.StringEncoder";
	public static String KAFKA_COMMON_ZK_CONNECT_VALUE = "kafka.common.zk.connetion";
	public static String KAFKA_COMMON_TIMEOUT_MS_VALUE = "";
	public static String KAFKA_COMMON_SOCKET_TIMEOUT_MS_VALUE = "";
	public static String KAFKA_COMMON_RECONNECT_INTERVAL_VALUE = "";
	public static String KAFKA_COMMON_PRODUCER_TYPE_VALUE = "";
	public static String KAFKA_COMMON_QUEUE_TIME_VALUE = "";
	public static String KAFKA_COMMON_QUEUE_SIZE_VALUE = "";
	public static String KAFKA_COMMON_BATCH_SIZE_VALUE = "";

	public static String CPS_FLASH_SALE = "http://59.151.100.36:8070/newcps/v1/sale/single";

	/** kafka 相关配置 end */

	public static String CPS_VIP_PRICE_BATCH = "http://59.151.100.36:8070/newcps/v1/price/vip_price/list";

	/**
	 * 获取VIP书籍价格，单本
	 */
	public static String CPS_VIP_PRICE = "http://59.151.100.36:8070/newcps/v1/price/vip_price";

	/** 批量获取书籍价格信息*/
	public static String CPS_BOOK_CHAPTER_PRICE_NEW_BATCH = "http://59.151.100.36:8070/cps/getBookChapterPriceNewBatch";

	/** 书籍vp资产校验*/
	public static String VP_BOOKASSET_VALIDATE="http://59.151.100.36:27000/vp/assets/judgeAssets";

	public static String CPS_SHOPPINGCART_ACTIVITY = "http://59.151.100.36:8070/cps/v1/get_cart_promotion";

}
