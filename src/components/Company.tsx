import React, { use, useEffect, useState } from 'react';

const Company: React.FC = () => {
    const [company,setCompany] = useState<any>([]);
    const [loading,setLoading] = useState(true);

    const userId = localStorage.getItem('userId');
    const api = "http://localhost:8080";

    useEffect(() => {
        fetchCompany
    }, [userId]);
    
    const fetchCompany = async () => {
        try {
            const res = await fetch(`${api}/api/company`);
            const data = await res.json();
            if(data) {
                setCompany(data)
            }
        } catch (err) {
            console.log("err: ", err);
        } finally {
            setLoading(false);
        }
    }
    return (
<></>
    );
};

export default Company;