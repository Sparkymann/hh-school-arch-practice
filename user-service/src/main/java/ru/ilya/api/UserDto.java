package ru.ilya.api;

public record UserDto(Long id, String name, Integer sentPackagesCount, Integer receivedPackagesCount) {
}
