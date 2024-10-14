
package acme.features.administrator.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.currency.Currency;
import acme.entities.systemConfiguration.SystemConfiguration;

@Service
public class AdministratorCurrencyCreateService extends AbstractService<Administrator, Currency> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorCurrencyRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Currency object;
		SystemConfiguration systemConfiguration;

		systemConfiguration = this.repository.findSystemConfiguration().stream().toList().get(0);

		object = new Currency();

		object.setSystemConfiguration(systemConfiguration);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Currency object) {
		assert object != null;

		super.bind(object, "symbol", "valueAgainstDollar");
	}

	@Override
	public void validate(final Currency object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("symbol") && object.getSymbol() != null) {

			Currency currency = this.repository.findCurrencyBySymbol(object.getSymbol());

			super.state(currency == null, "symbol", "administrator.currency.error.same-symbol");
		}
	}

	@Override
	public void perform(final Currency object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Currency object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "symbol", "valueAgainstDollar");

		super.getResponse().addData(dataset);
	}
}
