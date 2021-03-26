package main.model.repositories;

import main.model.ModerationStatus;
import main.model.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, Integer> {

  List<Posts> findAllByModerationStatus(ModerationStatus moderationStatus);

  @Query(
      "SELECT p.id, p.time, u.id, u.name, p.title, p.text, COUNT(pv1), COUNT(pv2), COUNT(pc), p.viewCount "
          + "FROM Posts p "
          + "LEFT JOIN Users u ON u.id = p.user "
          + "LEFT JOIN PostVotes pv1 ON p.id = pv1.postId AND pv1.value = 1 "
          + "LEFT JOIN PostVotes pv2 ON p.id = pv2.postId AND pv2.value = -1 "
          + "LEFT JOIN PostComments pc ON p.id = pc.postId "
          + "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() "
          + "GROUP BY p.id")
  Page<Posts> findAll(Pageable pageable);
}
