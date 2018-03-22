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

import util.Box;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static util.Box.empty;
import static util.Box.of;

public class FilterIterator<T> implements Iterator<T> {
    final Predicate<T> p;
    final Iterator<T> src;
    Box<T> curr;
    public FilterIterator(Iterable<T> src,Predicate<T> p) {
        this.src = src.iterator();
        this.p = p;
        curr = empty();
    }
    public boolean hasNext() {
        if (curr.isPresent()) return true;
        while(src.hasNext()){
            T item = src.next();
            if (p.test(item)) {
                curr = of(item);
                return true;
            }
        }
        return false;
    }
    public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        T aux = curr.getItem();
        curr = empty();
        return aux;
    }
}