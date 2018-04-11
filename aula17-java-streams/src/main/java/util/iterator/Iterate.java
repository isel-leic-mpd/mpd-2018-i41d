/*
 * Copyright (c) 2018 Miguel Gamboa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package util.iterator;

import java.util.Iterator;
import java.util.function.UnaryOperator;

public class Iterate<T> implements Iterator<T> {
    T seed;
    final UnaryOperator<T> op;

    public Iterate(T seed, UnaryOperator<T> op) {
        this.seed = seed;
        this.op = op;
    }
    public boolean hasNext() {return true;}
    public T next() {return seed = op.apply(seed);}
}
