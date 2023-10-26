package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.cognixia.jump.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer>  {

    News findByTitle(String title);
    
    @Query("SELECT n FROM News n ORDER BY n.pubDate ASC")
    List<News> findAllByOrderByPubDateAsc(Pageable pageable);
	
}

