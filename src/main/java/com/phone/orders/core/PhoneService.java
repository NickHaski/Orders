package com.phone.orders.core;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhoneService {

    @Value("${phone.service.url}")
    private String phoneServicePriceHost;

    private final RestTemplate restTemplate;

    public List<PhonePrice> getPhonePrices(final Set<Long> ids) {

        final String phoneServicePriceUrl = getUrlForPrices(ids);

        final PhonePriceList phonePriceList = restTemplate.getForObject(phoneServicePriceUrl, PhonePriceList.class);
        return phonePriceList != null && !CollectionUtils.isEmpty(phonePriceList.getPhonePrices()) ? phonePriceList.phonePrices : new ArrayList<>(0);
    }

    private String getUrlForPrices(final Set<Long> ids) {
        final Set<String> idsParam = ids.stream().map(String::valueOf).collect(Collectors.toSet());
        return UriComponentsBuilder.fromHttpUrl(phoneServicePriceHost)
                .queryParam("ids", String.join(",", idsParam))
                .build()
                .toUriString();
    }
}
