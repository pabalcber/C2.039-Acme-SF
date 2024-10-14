
package acme.features.authenticated.sponsor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.accounts.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.roles.Sponsor;

@Repository
public interface AuthenticatedSponsorRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select c from Sponsor c where c.userAccount.id = :id")
	Sponsor findOneSponsorByUserAccountId(int id);

}
