package com.guzzmega.springsecurity.domain.dto;

import java.util.List;

public record Feed(List<FeedItem> feedItemList,
                   int page,
                   int pageSize,
                   int totalPages,
                   long totalElements) {
}
