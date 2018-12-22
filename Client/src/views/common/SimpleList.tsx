import * as React from "react";
import {Panel, PanelGroup} from "react-bootstrap";

export interface IList<T> {
    list: T[];
    show: (x: T) => JSX.Element;
}

const SimpleList = function <T>({list, show}: IList<T>) { // tslint:disable-line
    return (
        <div>
            <PanelGroup accordion>
                {
                    list.map((c, i) => (
                        <Panel key={i} eventKey={i}>
                            {show(c)}
                        </Panel>
                    ))
                }
            </PanelGroup>
        </div>);
};

export default SimpleList;
