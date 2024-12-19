package model.balance;

import static java.time.format.TextStyle.FULL;

import java.text.NumberFormat;
import java.time.Month;
import java.time.Year;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderSales {
	
	private static final Locale LOCALE = Locale.getDefault();
	
	private static final NumberFormat nf = NumberFormat
			.getCurrencyInstance(LOCALE);
	
	private Year year;
	
	private int month;
	
	private double average;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append("year=");
		builder.append(year);
		builder.append(", month=");
		builder.append(Month.of(month).getDisplayName(FULL, LOCALE));
		builder.append(", average=");
		builder.append(nf.format(average));
		builder.append("]");
		return builder.toString();
	}
}
