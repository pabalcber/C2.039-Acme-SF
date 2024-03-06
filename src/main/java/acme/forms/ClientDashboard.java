
package acme.forms;

import acme.client.data.AbstractForm;
import acme.client.data.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {
	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	int							totalProgressLogsBelow25Percent;
	int							totalProgressLogs25To50Percent;
	int							totalProgressLogs50To75Percent;
	int							totalProgressLogsAbove75Percent;
	Money						avgBudget;
	Money						deviationBudget;
	Money						minimumBudget;
	Money						maximumBudget;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
