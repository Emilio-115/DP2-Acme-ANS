
package acme.entities.weather;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Weather extends AbstractEntity {
	//city,temperature,precepitation,windSpeed,cloudCover,snowfall,pressure

	@Mandatory
	@ValidString
	@Automapped
	private String	city;

	@Mandatory
	@ValidNumber(fraction = 1)
	@Automapped
	private double	temperature;

	@Mandatory
	@ValidNumber(fraction = 1)
	@Automapped
	private double	precipitation;

	@Mandatory
	@ValidNumber(fraction = 1)
	@Automapped
	private double	windSpeed;

	@Mandatory
	@ValidNumber
	@Automapped
	private int		cloudCover;

	@Mandatory
	@ValidNumber
	@Automapped
	private int		snowfall;

	@Mandatory
	@ValidNumber(fraction = 1)
	@Automapped
	private double	pressure;
}
