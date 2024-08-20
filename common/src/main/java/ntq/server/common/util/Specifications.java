package ntq.server.common.util;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.persistence.metamodel.SingularAttribute;

import java.util.List;

public class Specifications {
  private Specifications() {
  }

  public static <E> Specification<E> search(List<SingularAttribute<E, String>> attributes, String value) {
    if (value == null || attributes.isEmpty()) {
      return null;
    }
    return doSearch(attributes, value);
  }

  public static <E> Specification<E> doSearch(List<SingularAttribute<E, String>> attributes, String value) {
    var attr = doContain(attributes.get(0), value);
    for (int i = 1; i < attributes.size(); ++i) {
      attr = attr.or(doContain(attributes.get(i), value));
    }
    return attr;
  }

  public static <E, T> Specification<E> equals(SingularAttribute<E, T> attribute, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, builder) -> builder.equal(root.get(attribute), value);
  }

  public static <E, T> Specification<E> equals(String attribute, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, builder) -> builder.equal(root.get(attribute), value);
  }

  public static <E, T> Specification<E> notEquals(SingularAttribute<E, T> attribute, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, builder) -> builder.notEqual(root.get(attribute), value);
  }

  public static <E, T> Specification<E> notEquals(String attribute, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, builder) -> builder.notEqual(root.get(attribute), value);
  }

  public static <E> Specification<E> contain(SingularAttribute<E, String> attribute, String value) {
    if (!StringUtils.hasText(value)) {
      return null;
    }
    return doContain(attribute, value);
  }

  public static <E> Specification<E> doContain(SingularAttribute<E, String> attribute, String value) {
    return (root, query, builder) -> builder.like(builder.upper(root.get(attribute)), "%" + value.toUpperCase() + "%");
  }

  public static <E, T> Specification<E> in(SingularAttribute<E, T> attribute, List<T> values) {
    if (CollectionUtils.isEmpty(values)) {
      return null;
    }
    return (root, query, builder) -> root.get(attribute).in(values);
  }

  public static <E, T> Specification<E> isNull(SingularAttribute<E, T> attribute) {
    return (root, query, builder) -> builder.isNull(root.get(attribute));
  }

  public static <E> Specification<E> isNull(String attribute) {
    return (root, query, builder) -> builder.isNull(root.get(attribute));
  }

  public static <E, T> Specification<E> isNotNull(SingularAttribute<E, T> attribute) {
    return (root, query, builder) -> builder.isNotNull(root.get(attribute));
  }

  public static <E, T extends Comparable<? super T>> Specification<E> greaterThanOrEqualTo(SingularAttribute<E, T> attribute, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get(attribute), value);
  }

  public static <E, T extends Comparable<? super T>> Specification<E> greaterThanOrEqualTo(String attribute, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get(attribute), value);
  }

  public static <E, T extends Comparable<? super T>> Specification<E> greaterThan(SingularAttribute<E, T> attribute, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, builder) -> builder.greaterThan(root.get(attribute), value);
  }

  public static <E, T extends Comparable<? super T>> Specification<E> lessThan(SingularAttribute<E, T> attribute, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, builder) -> builder.lessThan(root.get(attribute), value);
  }

  public static <E, T extends Comparable<? super T>> Specification<E> lessThanOrEqualTo(SingularAttribute<E, T> attribute, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(attribute), value);
  }

  public static <E, T extends Comparable<? super T>> Specification<E> lessThanOrEqualTo(String attribute, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(attribute), value);
  }

  public static <E, F, T> Specification<E> equalsJoin(SingularAttribute<E, Long> joinAttr, Class<F> subEntity, SingularAttribute<F, Long> joinSubAttr, SingularAttribute<F, T> attribute, T value) {
    if (value == null) {
      return null;
    }
    return (root, query, builder) -> {
      var subquery = query.subquery(Integer.class);
      var subRoot = subquery.from(subEntity);
      subquery.select(builder.literal(1))
          .where(builder.equal(root.get(joinAttr), subRoot.get(joinSubAttr)), equals(attribute, value).toPredicate(subRoot, query, builder));
      return builder.exists(subquery);
    };
  }
}
