package com.link_intersystems.gradle.publication.plugins;

import org.gradle.api.Project;

import java.util.*;

public class PublicationUtils extends AbstractList<PublicationUtil> {

    public static PublicationUtils get() {
        ServiceLoader<PublicationUtil> serviceLoader = ServiceLoader.load(PublicationUtil.class, PublicationUtil.class.getClassLoader());
        Iterator<PublicationUtil> iterator = serviceLoader.iterator();
        List<PublicationUtil> utils = new ArrayList<>();
        iterator.forEachRemaining(utils::add);
        return new PublicationUtils(utils);
    }

    private List<? extends PublicationUtil> providers;

    PublicationUtils(List<? extends PublicationUtil> providers) {
        this.providers = providers;
    }

    @Override
    public PublicationUtil get(int index) {
        return providers.get(index);
    }

    @Override
    public int size() {
        return providers.size();
    }

    public void apply(Project project, PublicationServices publicationServices) {
        forEach(util -> util.apply(project, publicationServices));
    }
}
