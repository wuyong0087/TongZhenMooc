package com.tzhen.mooc.internal.di;

import com.tzhen.mooc.net.drupal.Endpoints;

import dagger.Component;

@Component(modules = DataModule.class)
public interface  DataComponent {
    Endpoints endpoints();
}
