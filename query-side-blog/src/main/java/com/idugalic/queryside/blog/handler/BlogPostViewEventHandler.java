package com.idugalic.queryside.blog.handler;

import com.idugalic.common.blog.event.BlogPostCreatedEvent;
import com.idugalic.common.blog.event.BlogPostPublishedEvent;
import com.idugalic.queryside.blog.domain.BlogPost;
import com.idugalic.queryside.blog.domain.BlogPost2;
import com.idugalic.queryside.blog.repository.BlogPost2Repository;
import com.idugalic.queryside.blog.repository.BlogPostRepository;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;

import org.axonframework.eventhandling.SequenceNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Event handlers for {@link BlogPostCreatedEvent}, {@link BlogPostPublishedEvent}
 * 
 * @author idugalic
 *
 */
@ProcessingGroup("default")
@Component
public class BlogPostViewEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BlogPostViewEventHandler.class);

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private BlogPost2Repository blogPost2Repository;

//    @EventHandler
//    public void handle(BlogPostCreatedEvent event, @SequenceNumber Long version) {
//        LOG.info("BlogPostCreatedEvent: [{}] ", event.getId());
//        blogPostRepository.save(new BlogPost(event, version));
//    }
//
//    @EventHandler
//    public void handle(BlogPostPublishedEvent event, @SequenceNumber Long version) {
//        LOG.info("BlogPostCreatedEvent: [{}] ", event.getId());
//        BlogPost post = blogPostRepository.findOne(event.getId());
//        post.setDraft(false);
//        post.setPublishAt(event.getPublishAt());
//        post.setVersion(version);
//        blogPostRepository.save(post);
//    }

    // demostration of one event, multiple query models
    @EventHandler
    public void handle2(BlogPostCreatedEvent event, @SequenceNumber Long version) {
        LOG.info("BlogPostCreatedEvent: [{}] ", event.getId());
        blogPost2Repository.save(new BlogPost2(event, version));
    }

    @EventHandler
    public void handle2(BlogPostPublishedEvent event, @SequenceNumber Long version) {
        LOG.info("BlogPostCreatedEvent: [{}] ", event.getId());
        BlogPost2 post = blogPost2Repository.findOne(event.getId());
        post.setDraft(false);
        post.setPublishAt(event.getPublishAt());
        post.setVersion(version);
        blogPost2Repository.save(post);
    }}
