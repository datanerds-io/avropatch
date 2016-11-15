package io.datanerds.avropatch.serialization;

import java.util.Objects;
import java.util.UUID;

public class Bimmel {

    public final String name;
    public final int number;
    public final UUID id;
    public final Bommel bommel;

    private Bimmel() {
        this.bommel = null;
        this.name = null;
        this.number = -42;
        this.id = null;
    }

    public Bimmel(String name, int number, UUID id, Bommel bommel) {
        this.name = name;
        this.number = number;
        this.id = id;
        this.bommel = bommel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bimmel bimmel = (Bimmel) o;
        return number == bimmel.number &&
                Objects.equals(name, bimmel.name) &&
                Objects.equals(id, bimmel.id) &&
                Objects.equals(bommel, bimmel.bommel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, number, id, bommel);
    }

    public static class Bommel {
        public final String name;

        private Bommel() {
            this.name = null;
        }

        public Bommel(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Bommel bommel = (Bommel) o;
            return Objects.equals(name, bommel.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
