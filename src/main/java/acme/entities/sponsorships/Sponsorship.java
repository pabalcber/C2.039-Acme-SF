
package acme.entities.sponsorships;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sponsorship extends AbstractEntity {

	// Serialisation identifier
	private static final long	serialVersionUID	= 1L;

	// Attributes
	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "[A-Z]{1,3}-//d{3}", message = "El código debe seguir el patrón '[A-Z]{1,3}-[0-9]{3}'")
	private String				code;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date				moment;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Future
	private Date				duration;

	@NotNull
	@Positive
	private double				amount;

	@NotNull
	private SponsorshipType		type;

	@Email
	@Length(max = 255)
	private String				contactEmail;

	@Length(max = 255)
	private String				link;

	// Relationships
	
}
