-- add field
ALTER TABLE public.mg_laptop ADD COLUMN store_id UUID;

-- add ids from table
UPDATE public.mg_laptop lp
SET store_id = st.id
FROM public.mg_store st
WHERE st.laptop_id = lp.id;

-- add constraint
ALTER TABLE public.mg_laptop
ADD CONSTRAINT fk_laptop_store_id_store
FOREIGN KEY (store_id)
REFERENCES public.mg_store (id);

-- drop fk constraint and column
DO $$
    DECLARE
    v_constraint_name TEXT;
    BEGIN
        SELECT con.constraint_name
        INTO v_constraint_name
        FROM information_schema.table_constraints con
            JOIN information_schema.key_column_usage kcu
                ON con.constraint_name = kcu.constraint_name
        WHERE con.constraint_type = 'FOREIGN KEY'
        AND kcu.table_name = 'mg_store'
        AND kcu.column_name = 'laptop_id';
    IF v_constraint_name IS NOT NULL THEN
        EXECUTE format('ALTER TABLE public.mg_store DROP CONSTRAINT %I', v_constraint_name);
    ELSE
        RAISE NOTICE 'No foreign key constraint found for mg_store.laptop_id';
    END IF;
END $$;

ALTER TABLE public.mg_store DROP COLUMN laptop_id;