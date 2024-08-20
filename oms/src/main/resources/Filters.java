package vn.com.mbbank.steady.core.util;

import org.springframework.data.jpa.domain.Specification;
import vn.com.mbbank.steady.common.util.Specifications;
import vn.com.mbbank.steady.core.model.entity.*;
import vn.com.mbbank.steady.core.model.filter.AccountHistoryFilter;
import vn.com.mbbank.steady.core.model.filter.BalanceFilter;
import vn.com.mbbank.steady.core.model.filter.CompanyFilter;

public class Filters {
  public static Specification<T> toSpecification(T t) {
    return null;
  }
  private Filters() {
    throw new UnsupportedOperationException();
  }
}
