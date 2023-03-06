import { MutableRefObject, useEffect } from "react"

type Props = {
    ref: MutableRefObject<any | null>;
    onEvent: () => void;
}

export const useOutsideAlert = ({ ref, onEvent } : Props) => {


    useEffect(() => {
        function handleClickOutside(event: any) {
            if(ref.current && !ref.current.contains(event.target)) {
                onEvent();
            }
        }
        document.addEventListener('mousedown', handleClickOutside);

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        }
        

    }, [ref]);
}