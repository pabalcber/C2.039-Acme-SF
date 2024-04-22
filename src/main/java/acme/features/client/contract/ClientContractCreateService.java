
package acme.features.client.contract;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.projects.Project;
import acme.roles.clients.Client;

@Service
public class ClientContractCreateService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository	repository;

	// AbstractService interface ----------------------------------------------

	private static String				budget	= "budget";


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Contract object;
		Client client;
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		client = this.repository.findClientById(super.getRequest().getPrincipal().getActiveRoleId());

		object = new Contract();
		object.setInstantiationMoment(moment);
		object.setDraftMode(true);
		object.setClient(client);
		object.setCustomerName(client.getIdentification());

		super.getBuffer().addData(object);
	}


	private static String invalidObject = "Invalid object: ";


	@Override
	public void bind(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException(ClientContractCreateService.invalidObject + object);

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		super.bind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", ClientContractCreateService.budget);
		object.setProject(project);
	}

	@Override
	public void validate(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException(ClientContractCreateService.invalidObject + object);

		this.validateUniqueCode(object);
		this.validateBudget(object);
	}

	@Override
	public void perform(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException(ClientContractCreateService.invalidObject + object);

		Date moment;
		Client client;

		client = this.repository.findClientById(super.getRequest().getPrincipal().getActiveRoleId());
		moment = MomentHelper.getCurrentMoment();

		object.setInstantiationMoment(moment);
		object.setCustomerName(client.getIdentification());
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		if (object == null)
			throw new IllegalArgumentException(ClientContractCreateService.invalidObject + object);

		Collection<Project> projects;
		SelectChoices choices;
		Dataset dataset;

		projects = this.repository.findManyProjects();
		choices = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", ClientContractCreateService.budget, "draftMode");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);
	}

	// Ancillary methods ------------------------------------------------------

	private void validateUniqueCode(final Contract object) {
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing = this.repository.findOneContractByCode(object.getCode());
			super.state(existing == null, "code", "client.contract.form.error.duplicated");
		}
	}

	private void validateBudget(final Contract object) {
		if (!super.getBuffer().getErrors().hasErrors(ClientContractCreateService.budget)) {
			Money b = object.getBudget();
			Project project = object.getProject();

			if (b == null) {
				super.state(false, ClientContractCreateService.budget, "client.contract.form.error.budget-cannot-be-null");
				return;
			}

			super.state(b.getAmount() >= 0, ClientContractCreateService.budget, "client.contract.form.error.negative-budget");

			if (project != null) {
				Money projectCost = project.getCost();

				if (!b.getCurrency().equals(projectCost.getCurrency()))
					super.state(false, ClientContractCreateService.budget, "client.contract.form.error.different-currency");

				if (b.getAmount() > projectCost.getAmount())
					super.state(false, ClientContractCreateService.budget, "client.contract.form.error.budget-exceeds-project-cost");
			}
		}
	}

}