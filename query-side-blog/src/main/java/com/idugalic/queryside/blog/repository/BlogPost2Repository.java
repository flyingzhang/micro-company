package com.idugalic.queryside.blog.repository;

import com.idugalic.common.blog.model.BlogPostCategory;
import com.idugalic.queryside.blog.domain.BlogPost;
import com.idugalic.queryside.blog.domain.BlogPost2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;

/**
 * A JPA repository for {@link BlogPost2}.
 * 
 * @author idugalic
 *
 */
@RepositoryRestResource(collectionResourceRel = "blogpost2s", path = "blogpost2s")
public interface BlogPost2Repository extends PagingAndSortingRepository<BlogPost2, String> {
    @Override
    @SuppressWarnings("unchecked")
    @RestResource(exported = false)
    BlogPost2 save(BlogPost2 entity);

    @Override
    @RestResource(exported = false)
    void delete(String aLong);

    @Override
    @RestResource(exported = false)
    void delete(BlogPost2 entity);

    Page<BlogPost2> findByDraftTrue(Pageable pageRequest);

    Page<BlogPost2> findByCategoryAndDraftFalse(@Param("category") BlogPostCategory category, Pageable pageable);

    Page<BlogPost2> findByDraftFalseAndPublishAtBeforeOrderByPublishAtDesc(@Param("publishedBefore") Date publishedBefore, Pageable pageRequest);

    Page<BlogPost2> findByCategoryAndDraftFalseAndPublishAtBefore(@Param("category") BlogPostCategory category, @Param("publishedBefore") Date publishedBefore,
                                                                 Pageable pageRequest);

    Page<BlogPost2> findByBroadcastAndDraftFalseAndPublishAtBefore(@Param("broadcast") boolean broadcast, @Param("publishedBefore") Date publishedBefore, Pageable pageRequest);

    Page<BlogPost2> findByDraftFalseAndPublishAtAfter(@Param("now") Date now, Pageable pageRequest);

    Page<BlogPost2> findByDraftFalseAndAuthorIdAndPublishAtBeforeOrderByPublishAtDesc(@Param("authorId") String authorId, @Param("publishedBefore") Date publishedBefore,
                                                                                     Pageable pageRequest);

    @Query("select p from BlogPost p where YEAR(p.publishAt) = ?1 and MONTH(p.publishAt) = ?2 and DAY(p.publishAt) = ?3")
    Page<BlogPost2> findByDateYearMonthDay(@Param("year") int year, @Param("month") int month, @Param("day") int day, Pageable pageRequest);

    @Query("select p from BlogPost p where YEAR(p.publishAt) = ?1 and MONTH(p.publishAt) = ?2")
    Page<BlogPost2> findByDateYearMonth(@Param("year") int year, @Param("month") int month, Pageable pageRequest);

    @Query("select p from BlogPost p where YEAR(p.publishAt) = ?1")
    Page<BlogPost2> findByDateYear(@Param("year") int year, Pageable pageRequest);

    BlogPost2 findByTitle(@Param("title") String title);

    BlogPost2 findByPublicSlugAndDraftFalseAndPublishAtBefore(@Param("publicSlug") String publicSlug, @Param("now") Date now);
}
