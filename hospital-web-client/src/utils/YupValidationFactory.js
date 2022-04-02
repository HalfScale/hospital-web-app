import * as Yup from 'yup';

export default function getYupValidation(schemaType) {
    if (schemaType === 'profileEdit') {

        return Yup.object().shape({
            firstName: Yup.string()
                .min(3, 'Too Short!')
                .max(50, 'Too Long!')
                .required('Required!'),
            lastName: Yup.string()
                .min(3, 'Too Short!')
                .max(50, 'Too Long!')
                .required('Required!'),
            mobileNo: Yup.string().min(11, 'Enter a correct mobile number')
            .max(11, 'Enter a correct mobile number').required('Required!')
            .matches(/^09[0-9]+$/, 'Mobile number format is incorrect'),
            birthDate: Yup.string().notRequired().nullable(),
            address: Yup.string().notRequired().nullable(),
            image: Yup.mixed().nullable()
                .test('is-correct-file', 'File too big', (file) => {
                    console.log('file', file);
                    let valid = true;
                    if (file) {
                        const size = file.size / 1024 / 1024
                        if (size > 10) {
                            valid = false
                        }

                    }
                    return valid
                })
        });
    }

    return null;
}