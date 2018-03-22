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

public class Limiter<T> implements Iterator<T> {
    final Iterator<T> iter;
    int n;
    public Limiter(Iterator<T> iter, int n) {
        this.iter = iter;
        this.n = n;
    }
    public boolean hasNext() {
        return n > 0 ? iter.hasNext() : false;
    }
    public T next() {
        if(n-- < 0) throw new IllegalStateException();
        return iter.next();
    }
}