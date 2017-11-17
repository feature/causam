package pw.stamina.causam.event;

public interface EventEmitter {

    <T> boolean emit(T event);
}
