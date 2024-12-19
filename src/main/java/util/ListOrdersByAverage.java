package util;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import infra.DAO;
import model.balance.OrderSales;

public class ListOrdersByAverage {
	
	static Logger log = LogManager.getLogger();
	
	public static void main(String[] args) {
		
		Configurator.initialize(ListOrdersByAverage.class.getName(),
				"./src/main/resources/log4j2.properties");
		
		DAO<OrderSales> dao = new DAO<>(OrderSales.class);
		
		List<OrderSales> search = dao.execute("Orders.AverageMonthly", 2024);
		search.forEach(log::info);
		
	}
	
}
