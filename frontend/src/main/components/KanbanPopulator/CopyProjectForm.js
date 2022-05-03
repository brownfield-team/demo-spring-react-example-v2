import { Button, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";

export default function CopyProjectForm(props) {
  const { onSubmit } = props;
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  return (
    <Form onSubmit={handleSubmit(onSubmit)}>
      <Form.Group className="mb-3">
        <Form.Label htmlFor="projName">New Project Name</Form.Label>
        <Form.Control
          id="projName"
          type="text"
          isInvalid={Boolean(errors.projName)}
          {...register("projName")}
        />
        <Form.Control.Feedback type="invalid">
          {errors.projName?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Button type="submit" data-testid="CopyProjectForm-Submit-Button">Populate Kanban Board</Button>
    </Form>
  );
}