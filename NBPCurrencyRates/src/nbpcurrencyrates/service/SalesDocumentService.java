package nbpcurrencyrates.service;

public class SalesDocumentService {
	public static void insert() {
		RatesService ratesService = new JsonURL();
		System.out.println(ratesService.getRates("2021-02-07", "USD"));
	}
	public static void main(String[] args) {
		insert();
	}
}
