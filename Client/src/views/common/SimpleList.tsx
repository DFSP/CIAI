import * as React from "react";

export interface IList<T> {
    title: string;
    list: T[];
    select: (x: T) => void;
    show: (x: T) => string;
}

const SimpleList = function <T>({title, list, show, select}: IList<T>) { // tslint:disable-line
    return (
        <div>
            <h1>{title}</h1>
            <ul>
                {list.map((c, i) => (<li key={i} onClick={() => select(c)}>
                        {show(c)}
                    </li>
                ))
                }
            </ul>
        </div>);
};

export default SimpleList;
