import * as React from "react";
import {ListGroup} from "react-bootstrap";

export interface IList<T> {
    title: string;
    list: T[];
    select: (x: T) => void;
    show: (x: T) => JSX.Element;
}

const SimpleList = function <T>({title, list, show, select}: IList<T>) { // tslint:disable-line
    return (
        <div>
            <ListGroup>
                {
                    list.map((c, i) => (
                        <div key={i} onClick={() => select(c)}>
                            {show(c)}
                        </div>
                    ))
                }
            </ListGroup>
        </div>);
};

export default SimpleList;
