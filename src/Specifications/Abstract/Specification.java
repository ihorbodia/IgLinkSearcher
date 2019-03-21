package Specifications.Abstract;

public interface Specification<T> {
    boolean isSatisfiedBy(T t);
}
