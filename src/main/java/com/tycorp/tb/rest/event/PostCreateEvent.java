package com.tycorp.tb.rest.event;

import com.tycorp.tb.rest.dto.non_exposable.PostCreateDto;
import com.tycorp.tb.domain.SignedInUserDetail;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;

@Getter(AccessLevel.PUBLIC)
public class PostCreateEvent extends AbstractAppEvent {

    private PostCreateDto createDto;
    private Long occuredAt = Instant.now().toEpochMilli();

    public PostCreateEvent(SignedInUserDetail signedInUserDetail,
                           String UUID,
                           Object source, PostCreateDto createDto) {
        super(source);
        this.signedInUserDetail = signedInUserDetail;
        this.UUID = UUID;

        this.createDto = createDto;
    }

    public static PostCreateEvent.Builder getBuilder() {
        return new PostCreateEvent.Builder();
    }
    public static class Builder {

        private SignedInUserDetail signedInUserDetail;
        public PostCreateEvent.Builder setOperator(SignedInUserDetail signedInUserDetail) {
            this.signedInUserDetail = signedInUserDetail;
            return this;
        }

        private Object source;
        private String UUID;

        private PostCreateDto createDto;

        public PostCreateEvent.Builder setSource(Object source) {
            this.source = source;
            return this;
        }

        public PostCreateEvent.Builder setUUID(String UUID) {
            this.UUID = UUID;
            return this;
        }

        public PostCreateEvent.Builder setCreateDto(PostCreateDto createDto) {
            this.createDto = createDto;
            return this;
        }

        public PostCreateEvent build() {
            return new PostCreateEvent(signedInUserDetail, UUID, source, createDto);
        }

    }

}
