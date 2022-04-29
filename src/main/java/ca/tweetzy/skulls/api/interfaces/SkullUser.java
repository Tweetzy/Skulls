package ca.tweetzy.skulls.api.interfaces;


import java.util.List;
import java.util.UUID;

/**
 * Date Created: April 04 2022
 * Time Created: 8:50 p.m.
 *
 * @author Kiran Hart
 */
public interface SkullUser extends DataSync{

	UUID getUUID();

	List<Integer> getFavourites();

	void toggleFavourite(int skullId);
}
