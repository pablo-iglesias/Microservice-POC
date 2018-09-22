package domain.usecase;

public abstract class Usecase<R extends Enum<R>> {

    public abstract R execute() throws Exception;
}
