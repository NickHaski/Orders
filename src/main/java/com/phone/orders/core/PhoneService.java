package com.phone.orders.core;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

        final Set<Long> existingPhones = Optional.ofNullable(phonePriceList)
                .map(PhonePriceList::getPhonePrices)
                .orElseGet(ArrayList::new)
                .stream()
                .map(PhonePrice::getId)
                .collect(Collectors.toSet());

        throwIfNoSomePhonesWasNotFound(ids, existingPhones);

        return Optional.ofNullable(phonePriceList)
                .filter(p -> CollectionUtils.isNotEmpty(p.getPhonePrices()))
                .map(PhonePriceList::getPhonePrices)
                .orElseGet(() -> new ArrayList<>(0));


    }

    private void throwIfNoSomePhonesWasNotFound(final Set<Long> ids, final Set<Long> foundedIds) {

        final Set<Long> missedPhones = ids.stream()
                .filter(foundedIds::contains)
                .collect(Collectors.toSet());

        if (CollectionUtils.isNotEmpty(missedPhones)) {
            throw new NoSomeOrderedPhonesException(String.format("No phone ids found:%s", missedPhones.toString()));
        }


    }

    private String getUrlForPrices(final Set<Long> ids) {
        final Set<String> idsParam = ids.stream().map(String::valueOf).collect(Collectors.toSet());
        return UriComponentsBuilder.fromHttpUrl(phoneServicePriceHost)
                .queryParam("ids", String.join(",", idsParam))
                .build()
                .toUriString();
    }
}
