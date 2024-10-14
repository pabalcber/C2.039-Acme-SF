
package acme.features.administrator.systemConfiguration;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.systemConfiguration.SystemConfiguration;

@Service
public class AdministratorSystemConfigurationUpdateService extends AbstractService<Administrator, SystemConfiguration> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorSystemConfigurationRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		SystemConfiguration systemConfiguration;

		systemConfiguration = this.repository.findSystemConfiguration().stream().toList().get(0);
		status = systemConfiguration != null && super.getRequest().getPrincipal().hasRole(Administrator.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		SystemConfiguration object;

		object = this.repository.findSystemConfiguration().stream().toList().get(0);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final SystemConfiguration object) {
		assert object != null;

		super.bind(object, "systemCurrency", "acceptedCurrencies");
	}

	@Override
	public void validate(final SystemConfiguration object) {
		assert object != null;

		Collection<String> symbols = this.repository.findCurrencySymbols();

		if (!super.getBuffer().getErrors().hasErrors("systemCurrency"))
			super.state(symbols.contains(object.getSystemCurrency()), "systemCurrency", "administrator.system-configuration.form.error.systemCurrency");
	}

	@Override
	public void perform(final SystemConfiguration object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final SystemConfiguration object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "systemCurrency", "acceptedCurrencies");

		super.getResponse().addData(dataset);
	}

}
