import { ReactNode } from "react";

type Default = {
    children?: ReactNode;
}

const Head = (props : Default) => <thead className="text-xs text-gray-700 uppercase bg-gray-200"><tr>{ props.children }</tr></thead>;
const HeadColumn = (props : Default) => <th scope="col" className="py-3 px-6">{ props.children }</th>;


const Body = (props : Default) => <tbody>{ props.children }</tbody>;
const BodyColumn = ({ children, ...rest } : Default) => <td className="py-4 px-6" {...rest}>{ children }</td>;
const BodyRow = (props : Default) => <tr className="bg-white border-b" {...props}>{ props.children }</tr>;

const Table =  ({ children } : Default) => {
    return (
        <div className="shadow-md rounded-md relative overflow-x-auto">
            <table className="w-full text-sm text-left text-gray-500">
                { children }
            </table>
        </div>
    );
}

function TableEmpty() {
    return (
        <tr className="bg-white border-b">
            <th className="py-4 px-6 font-medium text-gray-900 whitespace-nowrap" colSpan={5}>Lista vazia</th>
        </tr>
    );
}

Head.Column = HeadColumn;
Table.Head = Head;

Body.Row = BodyRow;
Body.Column = BodyColumn;
Table.Body = Body;

export default Table;