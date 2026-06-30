package com.example.poc.modules.updaters.domain.models;

public interface AfterSaleUpdater {
  boolean eligible(EventParameters parameters);
  void update(EventParameters parameters);
}
