
package acme.features.manager.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.userStories.Priority;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select count(u) from UserStory u where u.priority = :priority and u.project.manager.id = :managerId and u.draftMode=false")
	int priorityUserStories(int managerId, Priority priority);

	@Query("select avg(u.estimatedCost) from UserStory u where u.project.manager.id = :managerId and u.draftMode=false")
	Double averageEstimatedCost(int managerId);

	@Query("select stddev(u.estimatedCost) from UserStory u where u.project.manager.id = :managerId and u.draftMode=false")
	Double deviationEstimatedCost(int managerId);

	@Query("select min(u.estimatedCost) from UserStory u where u.project.manager.id = :managerId and u.draftMode=false")
	Double minimumEstimatedCost(int managerId);

	@Query("select max(u.estimatedCost) from UserStory u where u.project.manager.id = :managerId and u.draftMode=false")
	Double maximumEstimatedCost(int managerId);

	@Query("select avg(p.cost.amount) from Project p where p.cost.currency = :currency and p.manager.id = :managerId and p.draftMode=false")
	Double averageProjectCosts(String currency, int managerId);

	@Query("select stddev(p.cost.amount) from Project p where p.cost.currency = :currency and p.manager.id = :managerId and p.draftMode=false")
	Double deviationProjectCosts(String currency, int managerId);

	@Query("select min(p.cost.amount) from Project p where p.cost.currency = :currency and p.manager.id = :managerId and p.draftMode=false")
	Double minimumProjectCosts(String currency, int managerId);

	@Query("select max(p.cost.amount) from Project p where p.cost.currency = :currency and p.manager.id = :managerId and p.draftMode=false")
	Double maximumProjectCosts(String currency, int managerId);

}
