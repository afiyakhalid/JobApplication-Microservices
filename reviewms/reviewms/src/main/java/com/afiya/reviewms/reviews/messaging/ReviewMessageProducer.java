package com.afiya.reviewms.reviews.messaging;

import com.afiya.reviewms.reviews.Review;
import com.afiya.reviewms.reviews.dto.ReviewMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewMessageProducer {
    private RabbitTemplate rabbitTemplate;
    public ReviewMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Review review) {
 ReviewMessage reviewMessage=new ReviewMessage();
    reviewMessage.setId(review.getId());
     reviewMessage.setCompanyId(review.getCompanyId());
        reviewMessage.setDescription(review.getDescription());
        reviewMessage.setTitle(review.getTitle());
        rabbitTemplate.convertAndSend("companyRatingQueue",reviewMessage);
    }
}
