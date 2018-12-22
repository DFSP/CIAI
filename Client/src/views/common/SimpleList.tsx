import * as React from "react";
import {Panel, PanelGroup} from "react-bootstrap";

export interface IList<T> {
    list: T[];
    select: (x: T) => void;
    show: (x: T) => JSX.Element;
}

const SimpleList = function <T>({list, show, select}: IList<T>) { // tslint:disable-line
    return (
        <div>
            <PanelGroup accordion id="listWrapper">
                {
                    list.map((c, i) => (
                        <Panel key={i} eventKey={i} onClick={() => select(c)}>
                            {show(c)}
                        </Panel>
                    ))
                }
            </PanelGroup>
        </div>);
};

export default SimpleList;
