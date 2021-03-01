module NBPCurrencyRates {
	requires org.apache.httpcomponents.httpclient;
	requires org.apache.httpcomponents.httpcore;
	requires org.json;
	requires java.net.http;
	requires org.hibernate.orm.core;
	requires java.persistence;
	requires java.sql;
	requires junit;
	requires nv.i18n;
	exports nbpcurrencyrates.service;
	opens nbpcurrencyrates.service;
}